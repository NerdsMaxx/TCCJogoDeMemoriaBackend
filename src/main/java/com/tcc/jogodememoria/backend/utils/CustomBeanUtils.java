package com.tcc.jogodememoria.backend.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CustomBeanUtils {
    
    public static void copyNonNullProperties (Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
    
    public static boolean isAllNullProperty (Object source) {
        final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
        final PropertyDescriptor[] propertyDescriptorsSource = sourceBeanWrapper.getPropertyDescriptors();
        
        return (propertyDescriptorsSource.length - 1) == getNullPropertyNames(source).length;
    }
    
    public static boolean maybeHaveSomethingToPassToTarget (Object src, Object trg) {
        final BeanWrapper srcImpl = new BeanWrapperImpl(src);
        final BeanWrapper trgImpl = new BeanWrapperImpl(trg);
        
        final PropertyDescriptor[] propertiesSrc = srcImpl.getPropertyDescriptors();
        final PropertyDescriptor[] propertiesTrg = trgImpl.getPropertyDescriptors();
        
        for ( PropertyDescriptor propertySrc : propertiesSrc ) {
            String propertryNameSrc = propertySrc.getName();
            if ( propertryNameSrc.equals("class") ) {
                continue;
            }
            
            Object valueSrc = srcImpl.getPropertyValue(propertryNameSrc);
            if ( valueSrc == null ) {
                continue;
            }
            
            //@formatter:off
            if ( Arrays.stream(propertiesTrg).noneMatch((propertyTrg) -> propertyTrg.getName().compareTo(propertryNameSrc) == 0) ) {
                return true;
            }
            //@formatter:on
            
            Object valueTrg = trgImpl.getPropertyValue(propertryNameSrc);
            
            if ( ! valueSrc.equals(valueTrg) ) {
                return true;
            }
        }
        
        return false;
    }
    
    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
        final PropertyDescriptor[] propertyDescriptorsSource = sourceBeanWrapper.getPropertyDescriptors();
        
        Set<String> nullPropertiesSource = new HashSet<>();
        
        for ( PropertyDescriptor propertyDescriptor : propertyDescriptorsSource ) {
            
            Object sourceValue = sourceBeanWrapper.getPropertyValue(propertyDescriptor.getName());
            
            if ( sourceValue == null ) {
                nullPropertiesSource.add(propertyDescriptor.getName());
            }
        }
        
        final String[] result = new String[nullPropertiesSource.size()];
        return nullPropertiesSource.toArray(result);
    }
}
