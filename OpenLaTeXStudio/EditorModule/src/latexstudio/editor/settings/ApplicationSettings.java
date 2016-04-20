/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Properties;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.util.Exceptions;
import org.openide.util.Pair;

/**
 * A class representing settings of the application.
 * @author Sebastian
 */
public final class ApplicationSettings extends Properties {
    
    public static final ApplicationSettings INSTANCE = new ApplicationSettings();
    
    private final EnumMap<Setting,ArrayList<Pair<Object,Method>>> settingListeners = new EnumMap<>(Setting.class);
    
    private ApplicationSettings() {
        for( Setting s : Setting.values() ){
            settingListeners.put(s, new ArrayList<Pair<Object,Method>>());
        }
        load();
    }
    
    public void load() {        
        try {
            load( new FileInputStream( ApplicationUtils.getSettingsFile() ) );
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void save() {
        try {
            store( new FileOutputStream( ApplicationUtils.getSettingsFile() ), "Open LaTeX Studio application settings" );
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void registerSettingListeners(Object o) {
        for(Method m : o.getClass().getMethods()) {
            for(Annotation a : m.getAnnotationsByType( SettingListener.class )) {
                Setting s = ((SettingListener) a).setting();
                //TODO 20160323 Type validation
                settingListeners.get(s).add(Pair.of(o,m));
                try {
                    m.invoke(o, s.getValue());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
        }
    }
            
    /**
     * Publish the new setting value to all listeners. The type of <code>value</value> is assumed to match the setting.
     * @param setting
     * @param value
     */
    private void fireSettingChange(Setting setting, Object value) {
        for( Pair<Object,Method> p : settingListeners.get(setting)) {
            try {
                p.second().invoke(p.first(), value );
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    private String getSettingValue(Setting setting) {
        return getProperty(setting.storageName, setting.getDefaultValue());
    }
    
    private void setSettingValue(Setting setting, Object value) {
        setProperty(setting.storageName, value.toString());
        fireSettingChange(setting, value);
    }
    
    public static enum Setting {
        DROPBOX_TOKEN( "dropbox.token", "", SettingType.STRING ),
        LATEX_PATH( "latex.path", "", SettingType.STRING ),
        USER_LASTDIR( "user.lastdir", "", SettingType.STRING ),
        AUTOCOMPLETE_ENABLED( "autocomplete.enabled", "true", SettingType.BOOLEAN ),
        AUTOCOMPLETE_DELAY( "autocomplete.delay", "700", SettingType.INT ),
        LINEWRAP_ENABLED( "linewrap.enabled", "true", SettingType.BOOLEAN );
        
        
        private final String storageName;
        private final String defaultValue;
        private final SettingType valueType;
        
        Setting(String storageName, String defaultValue, SettingType valueType) {
            this.storageName = storageName;
            this.defaultValue = defaultValue;
            this.valueType = valueType;
        }
        
        public String getDefaultValue() {
            return defaultValue;
        }
        
        public Class<?> getSettingType() {
            return valueType.getValueClass();
        }
        
        public Object getValue() {
            String value = ApplicationSettings.INSTANCE.getSettingValue(this);
            Object typedValue = null;
            
            switch( valueType ) {
                case STRING:
                    typedValue = String.valueOf(value);
                    break;
                case INT:
                    typedValue = Integer.valueOf(value);
                    break;
                case BOOLEAN:
                    typedValue = Boolean.valueOf(value);
            }
            
            return typedValue;
        }
        
        public void setValue( Object value ) throws IllegalArgumentException {
            if( valueType.getValueClass().isInstance(value) ) {
                ApplicationSettings.INSTANCE.setSettingValue(this, value );
                ApplicationSettings.INSTANCE.fireSettingChange(this, value);
            } else {
                throw new IllegalArgumentException( "Supplied setting value: " + value.toString() + " is not an instance of the value type of " + this.toString() + " which is " + this.getSettingType() + "." );
            }
        }
    }
    
    /**
     * Wrapper class to restrict the possible value types of settings
     */
    public static enum SettingType {
        INT( Integer.class ),
        STRING( String.class ),
        BOOLEAN( Boolean.class);
        
        private final Class<?> valueClass;
        
        SettingType( Class<?> valueClass ) {
            this.valueClass = valueClass;
        }
        
        public Class<?> getValueClass() {
            return valueClass;
        }
    }
}
