package com.tcc.jogodememoria.backend.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CustomBeanUtils {

  private CustomBeanUtils() {}

  /**
   * Copies properties non null from one object to another.
   * @param source
   * @param target
   */
  public static void copyNonNullProperties(Object source, Object target) {
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  }

    /**
   * Verify if all property from object is null.
   * @param source
   */
  public static boolean isAllNullProperty(Object source) {
    final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
    final PropertyDescriptor[] propertyDescriptorsSource = sourceBeanWrapper.getPropertyDescriptors();

    return (propertyDescriptorsSource.length - 1) == getNullPropertyNames(source).length;
  }

  /**
   * Returns an array of null properties of an object.
   * @param source
   */
  private static String[] getNullPropertyNames(Object source) {
    final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
    final PropertyDescriptor[] propertyDescriptorsSource = sourceBeanWrapper.getPropertyDescriptors();

    Set<String> nullPropertiesSource = new HashSet<>();

    for (PropertyDescriptor propertyDescriptor : propertyDescriptorsSource) {
      //check if value of this property is null then add it to the collection
      Object sourceValue = sourceBeanWrapper.getPropertyValue(
        propertyDescriptor.getName()
      );

      if (sourceValue == null) {
        nullPropertiesSource.add(propertyDescriptor.getName());
      }
    }

    final String[] result = new String[nullPropertiesSource.size()];
    return nullPropertiesSource.toArray(result);
  }
}
