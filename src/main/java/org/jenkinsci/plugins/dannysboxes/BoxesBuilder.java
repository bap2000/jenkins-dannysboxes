package org.jenkinsci.plugins.dannysboxes;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Hudson;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BoxesBuilder extends Builder {
    
    private final boolean isMonolithic;
    private final ArrayList<Module> monolithic;
    private final ArrayList<Module> frontend;

    @DataBoundConstructor
    public BoxesBuilder(final boolean isMonolithic, final ArrayList<Module> monolithic, final ArrayList<Module> frontend) {
        this.isMonolithic = isMonolithic;
        this.monolithic = monolithic;
        this.frontend = frontend;
    }

    public boolean getIsMonolithic() {
        return isMonolithic;
    }

    public ArrayList<Module> getMonolithic() {
        return monolithic;
    }

    public ArrayList<Module> getFrontend() {
        return frontend;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        for (Module module : isMonolithic ? monolithic : frontend) {
            for (DannysCheckbox box : module.getTestCase())
                if (box.isSelected())
                    listener.getLogger().println("run testcase: " + box.getName() + " (in module: " + module.getName() + ")");
        }
        return true;
    }

    public DescriptorImpl getDescriptor() {
        return Hudson.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        private transient Document masterTestSuite;
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return Messages.displayName();
        }

        public ArrayList<Module> getDefaultMonolithic() {
            return getModules("mo");
        }
        
        public ArrayList<Module> getDefaultFrontend() {
            return getModules("fe");
        }
        
        public ArrayList<Module> getModules(final String mode) {
            try {
                return getModulesFor(mode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }
                
        private ArrayList<Module> getModulesFor(final String mode) throws IOException, DocumentException {
            final List<Node> testCases = getMasterTestSuite().selectNodes("//testsuite[@mode = '" + mode + "']", "@name");
            final Map<String, Module> modules = new TreeMap<String, Module>();
            for (Node testCase : testCases) {
                final String moduleName = testCase.valueOf("base/text()");
                Module module = modules.get(moduleName);
                if (module == null) {
                    module = new Module(moduleName, new ArrayList<DannysCheckbox>());
                    modules.put(moduleName, module);
                }
                module.addTestCase(new DannysCheckbox(testCase.valueOf("@name"), true));
            }
            return new ArrayList<Module>(modules.values());
        }
        
        private synchronized Document getMasterTestSuite() throws IOException, DocumentException {
            if (masterTestSuite != null) return masterTestSuite;
            final SAXReader reader = new SAXReader();
            final InputStream sampleTestcase = getClass().getResourceAsStream("testcase.xml");
            try {
                masterTestSuite = reader.read(sampleTestcase);
                return masterTestSuite;
            } finally {
                try {
                    if (sampleTestcase != null) sampleTestcase.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        
    }
    
}

