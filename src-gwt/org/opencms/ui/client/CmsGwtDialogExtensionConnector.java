/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.ui.client;

import org.opencms.gwt.client.ui.contenteditor.I_CmsContentEditorHandler;
import org.opencms.gwt.client.ui.contextmenu.CmsEditProperties;
import org.opencms.gwt.client.ui.contextmenu.I_CmsContextMenuCommand;
import org.opencms.gwt.client.ui.contextmenu.I_CmsContextMenuHandler;
import org.opencms.gwt.client.util.CmsDebugLog;
import org.opencms.ui.components.extensions.CmsGwtDialogExtension;
import org.opencms.ui.shared.components.I_CmsGwtDialogClientRpc;
import org.opencms.ui.shared.components.I_CmsGwtDialogServerRpc;
import org.opencms.util.CmsUUID;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Client side part of CmsGwtDialogExtension.<p>
 */
@Connect(CmsGwtDialogExtension.class)
public class CmsGwtDialogExtensionConnector extends AbstractExtensionConnector {

    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /** List of structure ids of changed resources. */
    protected List<String> m_changed = Lists.newArrayList();

    /**
     * Disposes of the extension on the server side and notifies the server of which resources have been changed.<p>
     */
    protected void close() {

        getRpcProxy(I_CmsGwtDialogServerRpc.class).onClose(m_changed);
    }

    /**
     * @see com.vaadin.client.extensions.AbstractExtensionConnector#extend(com.vaadin.client.ServerConnector)
     */
    @Override
    protected void extend(ServerConnector target) {

        registerRpc(I_CmsGwtDialogClientRpc.class, new I_CmsGwtDialogClientRpc() {

            /** Serial version id. */
            private static final long serialVersionUID = 1L;

            public void editProperties(String editStructureId) {

                CmsEditProperties.editProperties(new CmsUUID(editStructureId), new I_CmsContextMenuHandler() {

                    public boolean ensureLockOnResource(CmsUUID structureId) {

                        notImplemented();
                        return false;
                    }

                    public Map<String, I_CmsContextMenuCommand> getContextMenuCommands() {

                        notImplemented();
                        return null;
                    }

                    public I_CmsContentEditorHandler getEditorHandler() {

                        notImplemented();
                        return null;
                    }

                    public void leavePage(String targetUri) {

                        notImplemented();
                    }

                    public void refreshResource(CmsUUID structureId) {

                        CmsDebugLog.consoleLog("Refresh");

                        List<String> changed = Lists.newArrayList();
                        changed.add("" + structureId);
                        m_changed = changed;
                        close();
                    }

                    public void unlockResource(CmsUUID structureId) {

                        notImplemented();
                    }

                    private void notImplemented() {

                        throw new IllegalStateException("Not implemented");
                    }
                }, new Runnable() {

                    public void run() {

                        CmsDebugLog.consoleLog("Cancel");
                        close();
                    }
                });
            }
        });
    }

}
