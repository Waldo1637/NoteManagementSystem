package rms.view.util;

import javax.swing.text.*;

/**
 * A {@link StyledEditorKit} that specifies custom behavior for text wrapping
 * and tabs.
 *
 * @author Timothy
 */
public class CustomStyledEditorKit extends StyledEditorKit {

    private final ViewFactory myViewFactory;

    public CustomStyledEditorKit(final int tabSizeInPixels) {
        //Handle a few cases then delegate to StyledEditorKit.StyledViewFactory
        this.myViewFactory = new ViewFactory() {
            @Override
            public View create(Element elem) {
                String kind = elem.getName();
                if (kind != null) {
                    switch (kind) {
                        case AbstractDocument.ContentElementName:
                            return new WrappedLabelView(elem);
                        case AbstractDocument.ParagraphElementName:
                            return new CustomTabParagraphView(elem, tabSizeInPixels);
                        default:
                            break;
                    }
                }
                //fallback to super implementation
                return CustomStyledEditorKit.super.getViewFactory().create(elem);
            }
        };
    }

    @Override
    public ViewFactory getViewFactory() {
        return myViewFactory;
    }

    /**
     * Borrowed from: http://java-sl.com/tip_letter_wrap_java7.html
     */
    private static class WrappedLabelView extends javax.swing.text.LabelView {

        public WrappedLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            return View.X_AXIS == axis ? 0 : super.getMinimumSpan(axis);
        }
    }

    /**
     * Borrowed from: http://java-sl.com/tip_default_tabstop_size.html
     */
    private static class CustomTabParagraphView extends javax.swing.text.ParagraphView {

        private final int tabSizeInPixels;

        public CustomTabParagraphView(Element elem, int tabSizeInPixels) {
            super(elem);
            this.tabSizeInPixels = tabSizeInPixels;
        }

        @Override
        public float nextTabStop(float x, int tabOffset) {
            //if a tab set is defined, fall back to superclass implementation
            if (getTabSet() != null) {
                return super.nextTabStop(x, tabOffset);
            } else {
                return (float) (getTabBase() + (((int) x / tabSizeInPixels + 1) * tabSizeInPixels));
            }
        }
    }
}
