package rms.view.search;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import javax.swing.JDialog;

/**
 *
 * @author Timothy
 */
public class BaseDialogBeanInfo extends SimpleBeanInfo {
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor desc = new BeanDescriptor(BaseDialog.class);
        desc.setValue("containerDelegate", "getInnerContentPanel");
        return desc;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return super.getPropertyDescriptors();
    }
    
    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[] { Introspector.getBeanInfo(JDialog.class) };
        } catch (IntrospectionException ex) {
            return super.getAdditionalBeanInfo();
        }
    }
}