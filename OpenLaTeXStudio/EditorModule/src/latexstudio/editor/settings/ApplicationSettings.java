/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private static final String DROPBOX_TOKEN   = "dropbox.token";
    private static final String USER_LASTDIR    = "user.lastdir";
    private static final String LATEX_PATH      = "latex.path";
    public static final String AUTOCOMPLETE_DELAY = "autocomplete.delay";
    public static final String LINEWRAP_STATUS = "linewrap.status";
    
    private static final int DEFAULT_AUTO_COMPLETE_DELAY = 700;
    
    private final EnumMap<Setting,ArrayList<Pair<Object,Method>>> settingListeners = new EnumMap<>(Setting.class);
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
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
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    public void setDropboxToken(String token) {
        setProperty(DROPBOX_TOKEN, token);
    }
    
    public String getDropboxToken() {
        return getProperty(DROPBOX_TOKEN);
    }
    
    public void setLatexPath(String path) {
        setProperty(LATEX_PATH, path);
        
    }
    
    public String getLatexPath() {
        return getProperty(LATEX_PATH);
    }
    
    public void setUserLastDir(String dir) {
        setProperty(USER_LASTDIR, dir);
    }
    
    public String getUserLastDir() {
        return getProperty(USER_LASTDIR);
    }
    
    public void setAutoCompleteDelay(int delay){
        pcs.firePropertyChange(AUTOCOMPLETE_DELAY, getAutoCompleteDelay(), delay);
        setProperty(AUTOCOMPLETE_DELAY, String.valueOf(delay));
    }
    
    public int getAutoCompleteDelay(){
        try{
            return Integer.parseInt(getProperty(AUTOCOMPLETE_DELAY));
        }catch(NumberFormatException ex){
            return DEFAULT_AUTO_COMPLETE_DELAY;
        }
    }
    
    public void setLineWrapStatus(boolean lineWrapStatus){
        pcs.firePropertyChange(LINEWRAP_STATUS, getLineWrapStatus(), lineWrapStatus);
        setProperty(LINEWRAP_STATUS, String.valueOf(lineWrapStatus));
    }
    
    public boolean getLineWrapStatus(){
        return Boolean.parseBoolean(getProperty(LINEWRAP_STATUS));
    }
    
    public void registerSettingListeners(Object o) {
        for(Method m : o.getClass().getMethods()) {
            for(Annotation a : m.getAnnotationsByType( SettingListener.class )) {
                Setting s = ((SettingListener) a).setting();
                //TODO 20160323 Type validation
                settingListeners.get(s).add(Pair.of(o,m));
                try {
                    m.invoke(o, getSettingValue(s));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
        }
    }
            
    private void fireSettingChange(Setting setting, String value) {
        for( Pair<Object,Method> p : settingListeners.get(setting)) {
            try {
                p.second().invoke(p.first(), value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    public String getSettingValue(Setting setting) {
        return getProperty(setting.storageName, setting.getDefaultValue());
    }
    
    public void setSettingValue(Setting setting, String value) {
        fireSettingChange(setting, value);
        setProperty(setting.storageName, value);
    }
    
    public static enum Setting {
        AUTOCOMPLETE_ENABLED( "autocomplete.enabled", "true", SettingType.BOOLEAN );
        
        private final String storageName;
        private final String defaultValue;
        private final SettingType type;
        
        Setting(String storageName, String defaultValue, SettingType type) {
            this.storageName = storageName;
            this.defaultValue = defaultValue;
            this.type = type;
        }
        
        public String getDefaultValue() {
            return defaultValue;
        }
        
        public SettingType getSettingType() {
            return type;
        }
    }
    
    public static enum SettingType {
        INT,
        STRING,
        BOOLEAN;
    }
}
