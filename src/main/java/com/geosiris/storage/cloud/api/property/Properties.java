/*
Copyright 2019 GEOSIRIS

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.geosiris.storage.cloud.api.property;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Wini;

/**
 * Sub class of Properties should be named as : [XXX]Properties or [XXX]Property
 * Property values will automatically be filled from a '.ini' file.
 * The ini file path will be located by an environment variable : CONFIG_FILE_PATH_VAR_NAME
 *
 * The ini values can be overridden by env variables for each property value.
 *  Ex: with a property class named My_propClassProperty that has attributes named : FirstAttribute and second_Attribute,
 *      the env variables to override values are respectively : my_propclass_FirstAttribute and my_propclass_second_Attribute
 *
 *      => pattern for env var is : [classNameInLowerCase]_[attributeName]
 */
public abstract class Properties {
    public static Logger logger = LogManager.getLogger(Properties.class);

    public static final String CONFIG_FILE_PATH_VAR_NAME = "WS_CONFIG_INI_FILE_PATH";

    public Properties() {
        init("");
    }
    public Properties(String iniFileContent) {
        init(iniFileContent);
    }

    public void init(String iniFileContent) {
        String className = this.getClass().getSimpleName();
        className = className.replaceAll("Propert[yi](es)*", "");

        Field[] fields = this.getClass().getDeclaredFields();

        try {
            Wini iniFile = null;
            if(iniFileContent != null && iniFileContent.trim().length()>0){
                iniFile = new Wini(new ByteArrayInputStream(iniFileContent.getBytes()));
            }else {
                iniFile = new Wini(new File(System.getenv().get(CONFIG_FILE_PATH_VAR_NAME)));
            }

            for (Field f : fields) {
                f.setAccessible(true);
                f.set(this, iniFile.get(className.toLowerCase(), f.getName(), f.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Override with env variables
        for (Field f : fields) {
            String envVar = System.getenv(className.toLowerCase() + "_" + f.getName());
            if(envVar != null) {
                f.setAccessible(true);
                try {
                    f.set(this, envVar);
                } catch (IllegalArgumentException e) {
                    try {
                        Method fromStringMethod = null;
                        for(Method m : f.getType().getMethods()){
                            if((m.getName().compareToIgnoreCase("fromString") == 0
                                    || m.getName().compareToIgnoreCase("valueOf") == 0
                                    || m.getName().compareToIgnoreCase("parse" + f.getType()) == 0)
                                    && m.getParameterCount() == 1
                                    && m.getParameters()[0].getType() == String.class){
                                fromStringMethod = m;
                                break;
                            }
                        }
                        assert fromStringMethod != null;
                        f.set(this, fromStringMethod.invoke(f, envVar));
                    }catch (Exception e2){
                        logger.error("Failed to assign value '" + envVar + "' to property " + f.getName() + " because failed to set it from string. You should provide a method \"T fromString(String s)\" or \"T valueOf(String s)\" in the class.");
                        logger.error(e2.getMessage(), e2);
                    }
                }catch (IllegalAccessException e){
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public String toString() {
        String className = this.getClass().getSimpleName();
        className = className.replaceAll("Propert[yi](es)*", "");
        String result = "[" + className.toLowerCase() + "]\n";
        for (Field f : this.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                result += "" + f.getName() + "=" + f.get(this) + " \n";
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return result;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T extends Properties> T parseJson(String jsonContent, Class<T> propertyClass){
        Gson gson = new Gson();
        return (T) gson.fromJson(jsonContent, propertyClass);
    }
}