package com.example.module_scanidbankcard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * 图片处理工具类
 */
public class ImageUtils {


    public static Uri resizeBitmap(Context ctx, Uri inputImage) {
        try {

            InputStream in = ctx.getContentResolver().openInputStream(
                    inputImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = options.outWidth, height_tmp = options.outHeight;

            // decode full image pre-resized
            in = ctx.getContentResolver().openInputStream(inputImage);
            options = new BitmapFactory.Options();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            Point sizeScreen = UIUtils.getSysScreen(ctx);
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < Math.min(REQUIRED_SIZE, sizeScreen.x)
                        || height_tmp / 2 < Math.min(REQUIRED_SIZE,
                        sizeScreen.y)) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            options.inSampleSize = scale;
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, width_tmp, height_tmp);
            RectF outRect = new RectF(0, 0, width_tmp / scale, height_tmp
                    / scale);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                    (int) (roughBitmap.getWidth() * values[0]),
                    (int) (roughBitmap.getHeight() * values[4]), true);

            File ofile = new File(FileCacheUtils.getCacheDirectory(ctx),
                    "tmp_wish_" + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            ImageUtils.saveBitmap(ctx, resizedBitmap, Uri.fromFile(ofile));

            roughBitmap.recycle();
            return Uri.fromFile(ofile);
        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
        }

        return null;
    }

    public static void saveBitmap(final Context ctx, final Bitmap bmp, final Uri outputImage, final HxCallBack<Boolean> callBack) {
        HxExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final boolean isSucess = saveBitmap(ctx, bmp, outputImage);
                HandlerUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.call(isSucess);
                    }
                });
            }
        });

    }

    private static boolean saveBitmap(Context ctx, Bitmap bmp, Uri outputImage) {
        try {
            OutputStream outputStream = ctx.getContentResolver().openOutputStream(outputImage);

            bmp.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.e("Image", e.getMessage(), e);
            return false;
        }

    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
                img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    /**
     * 将Uri地址装成Bitmap
     *
     * @param ctx
     * @param selectedImage
     * @param isThumb
     * @return
     * @throws IOException
     */
    public static Bitmap decodeUri(Context ctx, Uri selectedImage,
                                   boolean isThumb) throws IOException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                ctx.getContentResolver().openInputStream(selectedImage), null,
                o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = isThumb ? 256 : 1024;

        Point sizeScreen = UIUtils.getSysScreen(ctx);

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < Math.min(REQUIRED_SIZE, sizeScreen.x)
                    || height_tmp / 2 < Math.min(REQUIRED_SIZE, sizeScreen.y)) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bm = BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(selectedImage), null, o2);

        // 此处根据情况决定旋转图片

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bm, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bm, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bm, 270);
            default:
                return bm;
        }

    }

    /**
     * 将图片转成字节流
     *
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 5) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 5;
        }
        return output.toByteArray();
    }

    public static String bitmap2StrByBase64(Bitmap bit) {
        if (bit == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String bitmapEncodeBase64(byte[] bitmapBytes) {
        if (bitmapBytes == null)
            return "";

        String encoded = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        return encoded;
    }

    public static void compressImage(final Context context, final Uri imageUri, final int reqHeight, final int reqWidth, final int reqSizeUnitK, final HxCallBack<byte[]> callBack) {
        if (imageUri == null)
            return;

        HxExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final byte[] imageBytes = compressImages(context, imageUri, reqHeight, reqWidth, reqSizeUnitK);
                HandlerUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.call(imageBytes);
                    }
                });
            }
        });
    }

    /**
     * 对图片做压缩处理
     *
     * @param reqHeight
     * @param reqWidth
     */
    private static byte[] compressImages(final Context context, Uri imageUri, int reqHeight, int reqWidth, int reqSizeUnitK) {
        byte[] imageBytes = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(
                    imageUri), null, options);
            // 源图片的高度和宽度
            final int height = options.outHeight;
            final int width = options.outWidth;

            //当源图宽或高超出了要求的尺寸，进行压缩
            if (height > reqHeight || width > reqWidth) {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(
                        imageUri), null, options);
                bitmap = scale(bitmap, reqWidth, reqHeight, true);

                //压缩图片质量
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int quality = 100;
                do {
                    out.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    quality -= 5;
                } while (out.toByteArray().length / 1024 > reqSizeUnitK);
                imageBytes = out.toByteArray();
                out.close();
                bitmap.recycle();
            } else {
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri), null, options);
                imageBytes = bitmap2Bytes(bitmap);
                bitmap.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }catch (NullPointerException e){

        }
        return imageBytes;
    }

    /**
     * 对图片进行初步粗略的压缩，压缩出来的图片宽高会比reqWidth、reqHeight稍大
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 精确的压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     */
    public static Bitmap scale(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 根据想要的尺寸精确计算压缩比例
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        // 调用api中的方法进行压缩
        Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return scaleBitmap;
    }

//    /**
//     * 对图片做压缩处理,图片的大小小于150K，避免上传多张图片时内存溢出crash
//     *
//     * @param reqHeight
//     * @param reqWidth
//     */
//    private static byte[] doCompressImage(Context context, Uri imageUri, int reqHeight, int reqWidth, int reqSizeUnitK) {
//        byte[] imageBytes = null;
//        try {
//            InputStream in = context.getContentResolver().openInputStream(
//                    imageUri);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(in, null, options);
//            in.close();
//            in = null;
//            // 源图片的高度和宽度
//            final int height = options.outHeight;
//            final int width = options.outWidth;
//            //当源图宽或高超出了要求的尺寸，进行压缩
//            if (height * width / 1024.0 > reqSizeUnitK) {
//                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//                options.inJustDecodeBounds = false;
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                in = context.getContentResolver().openInputStream(
//                        imageUri);
//                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
//                in.close();
//                in = null;
//
//                // 根据想要的尺寸精确计算压缩比例
//                // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
//                float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
//                float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
//                sx = (sx < sy ? sx : sy);
//                sy = sx;// 哪个比例小点，就用哪个比例
//                Matrix matrix = new Matrix();
//                matrix.postScale(sx, sy);
//                // 调用api中的方法进行压缩
//                Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                bitmap.recycle();
//
//                //压缩图片至reqSizeUnitK KB以下
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                int quality = 100;
//                do {
//                    out.reset();
//                    scaleBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
//                    quality -= 5;
//                }while (out.toByteArray().length / 1024 > reqSizeUnitK);
//
//                imageBytes = out.toByteArray();
//                out.close();
////                scaleBitmap.recycle();
//            } else {
//                options.inJustDecodeBounds = false;
//                in = context.getContentResolver().openInputStream(
//                        imageUri);
//                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
//                in.close();
//                in = null;
//                imageBytes = bitmap2Bytes(bitmap);
//                bitmap.recycle();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return imageBytes;
//    }
//
//    /**
//     * 对图片进行初步粗略的压缩，压缩出来的图片宽高会比reqWidth、reqHeight稍大
//     *
//     * @param options
//     * @param reqWidth
//     * @param reqHeight
//     * @return
//     */
//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//    /**
//     * 精确的压缩图片
//     *
//     * @param bitmap   源图片
//     * @param width    想要的宽度
//     * @param height   想要的高度
//     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
//     * @return Bitmap
//     */
//    public static Bitmap scale(Bitmap bitmap, int width, int height, boolean isAdjust) {
//        // 根据想要的尺寸精确计算压缩比例
//        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
//        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
//        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
//        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
//            sx = (sx < sy ? sx : sy);
//            sy = sx;// 哪个比例小点，就用哪个比例
//        }
//        Matrix matrix = new Matrix();
//        matrix.postScale(sx, sy);
//        // 调用api中的方法进行压缩
//        Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        bitmap.recycle();
//        return scaleBitmap;
//    }


    public static String getfilePath(Context context, String imageNm) {
        String file = "";
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState()))
            file = "/mnt/sdcard/ZTB/" + imageNm;

        else
            file = context.getCacheDir().getAbsolutePath() + "/" + imageNm;

        return file;
    }

    /**
     * 保存图片到本地
     */
    public static void saveBitmap(Context context, Bitmap bm, String picName) {

        String path = "";
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState()))
            path = "/mnt/sdcard/ZTB/";

        else
            path = context.getCacheDir().getAbsolutePath() + "/";

        File file = new File(path);
        if (!file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        writeBitmap(path, picName, bm);

    }


    /**
     * 保存图片
     *
     * @param path
     * @param name
     * @param bitmap
     */
    public static void writeBitmap(String path, String name, Bitmap bitmap) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        File _file = new File(path + name);
        if (_file.exists()) {
            _file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_file);
            if (name != null && !"".equals(name)) {
                int index = name.lastIndexOf(".");
                if (index != -1 && (index + 1) < name.length()) {
                    String extension = name.substring(index + 1).toLowerCase();
                    if ("png".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } else if ("jpg".equals(extension)
                            || "jpeg".equals(extension)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }

    //获取和白色混合颜色
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite(green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    // 获取单色的混合值
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }
}