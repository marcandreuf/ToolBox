package org.mandfer.tools.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mandfer.tools.validation.ImageValidator;
import org.mandfer.tools.validation.ImageValidatorRegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by marc on 02/04/16.
 */
public class ImageValidatorTest {

    private static Logger logger = LoggerFactory.getLogger(ImageValidatorTest.class);

    private ImageValidator imageValidator;
    private String[] validImageFileNames;
    private String[] invalidImageFileNames;

    @Before
    public void initData(){
        imageValidator = new ImageValidatorRegExp();

        validImageFileNames = new String[] {
                "a.jpg", "a.gif","a.png", "a.bmp",
                "..jpg", "..gif","..png", "..bmp",
                "a.JPG", "a.GIF","a.PNG", "a.BMP",
                "a.JpG", "a.GiF","a.PnG", "a.BmP",
                "jpg.jpg", "gif.gif","png.png", "bmp.bmp",
                "a.mp4", "a.mov", "a.MP4", "a.MOV",
                "a.jpeg", "a.webp", "a.psd",
                "a.JPEG", "a.WEBP", "a.PSD",
                "a.ico", "a.ICO", "a.pcx", "a.PCX",
                "a.nef", "a.NEF", // Nikon
                "a.cr2", "a.CR2", // Canon
                "a.orf", "a.ORF", // Olympus
                "a.arw", "a.ARW", // Sony
                "a.rw2", "a.RW2", // Panasonic
                "a.rwl", "a.RWL", // Leica
                "a.srw", "a.SRW"  // Samsung
        };

        invalidImageFileNames = new String[] {
                ".jpg", ".gif",".png",".bmp",
                " .jpg", " .gif"," .png"," .bmp",
                "a.txt", "a.exe","a.","a.mp3",
                "jpg", "gif","png","bmp", ".JPG.bts"
        };
    }

    @Test
    public void ValidImageTest() {
        for(String fileName : validImageFileNames){
            boolean valid = imageValidator.validate(fileName);
            Assert.assertEquals("Filename " + fileName + " , is not supported." , true, valid);
        }
    }

    @Test
    public void InValidImageTest() {
        for(String fileName : invalidImageFileNames){
            boolean valid = imageValidator.validate(fileName);
            Assert.assertEquals("Filename " + fileName + " , is actually supported.", false, valid);
        }
    }
}
