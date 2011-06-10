package org.jenkinsci.plugins.dannysboxes;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;

public class Module implements Describable<Module> {
    
    private final String name;
    private final ArrayList<DannysCheckbox> testCase;

    @DataBoundConstructor
    public Module(final String name, final ArrayList<DannysCheckbox> testCase) {
        this.name = name;
        this.testCase = testCase;
    }

    public String getName() {
        return name;
    }

    public ArrayList<DannysCheckbox> getTestCase() {
        return testCase;
    }
    
    protected void addTestCase(final DannysCheckbox testCaseBox) {
        testCase.add(testCaseBox);
    }

    public DescriptorImpl getDescriptor() {
        return Hudson.getInstance().getDescriptorByType(DescriptorImpl.class);
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Module> {

        @Override
        public String getDisplayName() {
            return "Test Module";
        }
        
    }
}
