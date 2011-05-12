package org.jenkinsci.plugins.dannysboxes;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;

public class DannysCheckbox implements Describable<DannysCheckbox> {
    private final String name;
    private final boolean selected;

    @DataBoundConstructor
    public DannysCheckbox(final String name, final boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public Descriptor<DannysCheckbox> getDescriptor() {
        return Hudson.getInstance().getDescriptor(getClass());
    }
    
    @Extension
    public static class DescriptorImpl extends Descriptor<DannysCheckbox> {

        @Override
        public String getDisplayName() {
            return "A Box!";
        }

    }
}
