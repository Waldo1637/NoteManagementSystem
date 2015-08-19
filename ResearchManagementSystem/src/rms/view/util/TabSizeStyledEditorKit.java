package rms.view.util;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * Borrowed from: http://java-sl.com/tip_default_tabstop_size.html
 *
 * @author Timothy
 */
public class TabSizeStyledEditorKit extends StyledEditorKit {

    public final int tabSize;

    public TabSizeStyledEditorKit(int tabSizeInPixels) {
        this.tabSize = tabSizeInPixels;
    }

    @Override
    public ViewFactory getViewFactory() {
        return myViewFactory;
    }

    private final ViewFactory myViewFactory = new ViewFactory() {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new CustomTabParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            return new LabelView(elem);
        }
    };

    private class CustomTabParagraphView extends ParagraphView {

        public CustomTabParagraphView(Element elem) {
            super(elem);
        }

        @Override
        public float nextTabStop(float x, int tabOffset) {
            //if a tab set is defined, fall back to superclass implementation
            if (getTabSet() != null) {
                return super.nextTabStop(x, tabOffset);
            }

            return (float) (getTabBase() + (((int) x / tabSize + 1) * tabSize));
        }
    }
}
