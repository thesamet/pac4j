/*
  Copyright 2012 - 2013 Jerome Leleu

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
package org.pac4j.oauth.profile.linkedin2;

import org.pac4j.oauth.profile.XmlHelper;
import org.pac4j.oauth.profile.XmlObject;

/**
 * This class represents a LinkedIn company.
 * 
 * @author Jerome Leleu
 * @since 1.4.1
 */
public class LinkedIn2Company extends XmlObject {
    
    private static final long serialVersionUID = -2516111031032736648L;
    
    private String name;
    
    private String industry;
    
    @Override
    protected void buildFromXml(final String xml) {
        this.name = XmlHelper.get(xml, "name");
        this.industry = XmlHelper.get(xml, "industry");
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getIndustry() {
        return this.industry;
    }
}
