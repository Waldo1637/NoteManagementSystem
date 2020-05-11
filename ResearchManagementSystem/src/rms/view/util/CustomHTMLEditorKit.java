package rms.view.util;

import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * A {@link HTMLEditorKit} that specifies custom behavior for text wrapping and
 * tabs.
 *
 * @author Timothy
 */
public final class CustomHTMLEditorKit extends HTMLEditorKit {

    private final ViewFactory myViewFactory;

    public CustomHTMLEditorKit(final int tabSizeInPixels) {
        //Handle a few cases then delegate to HTMLEditorKit.HTMLFactory
        this.myViewFactory = new HTMLEditorKit.HTMLFactory() {
            @Override
            public View create(Element elem) {
                AttributeSet attrs = elem.getAttributes();
                Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
                Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
                if (HTML.Tag.CONTENT == o) {
                    return new WrappedInlineView(elem);
                } else if (HTML.Tag.P == o) {
                    return new CustomTabParagraphView(elem, tabSizeInPixels);
                }
                //fallback to super implementation
                return super.create(elem);
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
    private static class WrappedInlineView extends javax.swing.text.html.InlineView {

        public WrappedInlineView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            return View.X_AXIS == axis ? 0 : super.getMinimumSpan(axis);
        }
    }

    /**
     * Borrowed from: http://java-sl.com/tip_default_tabstop_size.html
     *
     * NOTE: an alternative to this would be setting the TabStop property on the
     * Element which can be retrieved via StyleConstants.getTabSet(attrs).
     */
    private static class CustomTabParagraphView extends javax.swing.text.html.ParagraphView {

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
