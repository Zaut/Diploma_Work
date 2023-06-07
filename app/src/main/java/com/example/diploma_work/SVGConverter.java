package com.example.diploma_work;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SVGConverter {
    public static void displaySVGImage(byte[] svgBytes, ImageView imageView) {
        InputStream inputStream = new ByteArrayInputStream(svgBytes);
        try {
            SVG svg = SVG.getFromInputStream(inputStream);
            Picture picture = svg.renderToPicture();
            Drawable drawable = new PictureDrawable(picture);

            // Отобразить картинку в SVGImageView (требуется AndroidSVG)
            if (imageView instanceof SVGImageView) {
                SVGImageView svgImageView = (SVGImageView) imageView;
                svgImageView.setSVG(svg);
            }
            // Или отобразить картинку в обычный ImageView
            else {
                imageView.setImageDrawable(drawable);
            }
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }
}
