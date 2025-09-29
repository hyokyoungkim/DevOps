package com.ithows.util;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Offscreen {
    public static boolean sDebug = false;
    private static int sDepth;
    private static int sWidth;
    private static int sHeight;
    private static Image sImage;
    private static Graphics sGraphics;
    private Component fComponent;
    private Dimension fComponentSize;
    private int fRender;

    private static synchronized void releaseResources() {
        if (sGraphics != null) {
            sGraphics.dispose();
            sGraphics = null;
        }

        if (sImage != null) {
            sImage.flush();
            sImage = null;
        }

    }

    public static boolean isRendering() {
        return sDepth > 0;
    }

    public Offscreen(Component var1) {
        this.fComponent = var1;
    }

    public synchronized Graphics startRender(Graphics var1) {
        if (sDebug) {
            this.acquire();
        }

        ++sDepth;
        if (sDepth > 1) {
            return var1;
        } else {
            this.fComponentSize = this.fComponent.getSize();
            if (sImage == null || this.fComponentSize.width > sWidth || this.fComponentSize.height > sHeight) {
                releaseResources();
                sWidth = Math.max(this.fComponentSize.width, sWidth);
                sHeight = Math.max(this.fComponentSize.height, sHeight);
                if (sDebug) {
                    System.out.println("resizing offscreen to " + sWidth + ", " + sHeight);
                }

                sImage = this.fComponent.createImage(sWidth, sHeight);
                if (sImage == null) {
                    --sDepth;
                    if (sDebug) {
                        this.release();
                    }

                    return null;
                }

                sGraphics = sImage.getGraphics();
            }

            Rectangle var2 = var1.getClipBounds();
            if (var2 != null) {
                sGraphics.setClip(var2);
            } else {
                sGraphics.setClip(0, 0, sWidth, sHeight);
            }

            return sGraphics;
        }
    }

    public synchronized void finishRender(Graphics var1) {
        --sDepth;
        if (sDepth == 0) {
            Rectangle var2 = var1.getClipBounds();
            if (var2 == null) {
                var2 = new Rectangle(0, 0, this.fComponentSize.width, this.fComponentSize.height);
            }

            var1.drawImage(sImage, var2.x, var2.y, var2.x + var2.width, var2.y + var2.height, var2.x, var2.y, var2.x + var2.width, var2.y + var2.height, this.fComponent);
        }

        if (sDebug) {
            this.release();
        }

    }

    private void acquire() {
        if (this.fRender > 0) {
            System.out.println("startRender re-entered " + this.fRender);
        }

        ++this.fRender;
    }

    private void release() {
        --this.fRender;
    }
}
