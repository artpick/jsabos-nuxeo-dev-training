package com.julian.sabos.test.utils;

import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.PartialDeploy;
import org.nuxeo.runtime.test.runner.TargetExtensions;

@Deploy({"org.nuxeo.ecm.automation.core", "org.nuxeo.ecm.automation.scripting", "com.julian.sabos.jsabos-nuxeo-dev-training-core"})
@PartialDeploy(bundle = "studio.extensions.jsabos-SANDBOX", extensions = {TargetExtensions.ContentModel.class})
public class AutomationCustom extends PlatformFeature {
}