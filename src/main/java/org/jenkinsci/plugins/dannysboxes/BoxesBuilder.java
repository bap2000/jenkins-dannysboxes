package org.jenkinsci.plugins.dannysboxes;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link BoxesBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class BoxesBuilder extends Builder {
    
    private final boolean isMonolithic;
    private final ArrayList<DannysCheckbox> monolithic;
    private final ArrayList<DannysCheckbox> frontend;

    @DataBoundConstructor
    public BoxesBuilder(final boolean isMonolithic, final ArrayList<DannysCheckbox> monolithic, final ArrayList<DannysCheckbox> frontend) {
        this.isMonolithic = isMonolithic;
        this.monolithic = monolithic;
        this.frontend = frontend;
    }

    public boolean isMonolithic() {
        return isMonolithic;
    }

    public ArrayList<DannysCheckbox> getMonolithic() {
        return monolithic;
    }

    public ArrayList<DannysCheckbox> getFrontend() {
        return frontend;
    }

    private final boolean isBoxChecked(final String boxName) {
        for (DannysCheckbox box : isMonolithic ? monolithic : frontend) {
            if (boxName.equals(box.getName())) return box.isSelected();
        }
        return false;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        for (DannysCheckbox box : isMonolithic ? monolithic : frontend)
            if (box.isSelected())
                listener.getLogger().println("Someone chose box: " + box.getName() + "!");
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "dannyD_ haz boxes!";
        }

        public ArrayList<DannysCheckbox> getDefaultMonolithic() {
            return new ArrayList<DannysCheckbox> (Arrays.asList(
              new DannysCheckbox("something big", true),  
              new DannysCheckbox("something else big", false),  
              new DannysCheckbox("choose me I'm big", true)  
            ) );
        }
        
        public ArrayList<DannysCheckbox> getDefaultFrontend() {
            return new ArrayList<DannysCheckbox> (Arrays.asList(
              new DannysCheckbox("frontend option 1", false),  
              new DannysCheckbox("frontend 2", false),  
              new DannysCheckbox("You really want me", true)  
            ) );
        }
        
        public Class<DannysCheckbox> getBoxClass() {
            return DannysCheckbox.class;
        }
    }
}

