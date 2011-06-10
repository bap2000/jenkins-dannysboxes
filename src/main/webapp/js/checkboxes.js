
function db_showTestType(isMonolithic) {
    var outerTable = $(isMonolithic).ancestors()[3];
    var monolithic = outerTable.down("tr", 0).next().next().next().next();
    var frontend = monolithic.next().next();
    if (isMonolithic.checked) {
        monolithic.show();
        frontend.hide();
    } else {
        monolithic.hide();
        frontend.show();
    }
}

function db_setall(link, onOrOff) {
    $(link).ancestors()[2].getElementsBySelector("input").each(function(box) {
        box.checked = onOrOff;
    });
}

function db_hideOrExpandModule(moduleName) {
    var thisModule = $(moduleName).ancestors()[1];
    var tr = thisModule.next();
    if (tr.visible()) {
        while (tr != null) {
            tr.hide();
            tr = tr.next();
        }
    } else {
        var allModulesContainer = thisModule.ancestors()[3];
        db_collapseModules(allModulesContainer.getElementsBySelector("a.moduleName"));
        while (tr != null) {
            tr.show();
            tr = tr.next();
        }
    }
}

function db_collapseModules(moduleNameLinks) {
    moduleNameLinks.each(function(link) {
        var tr = link.ancestors()[1];
        tr = tr.next();
        while (tr != null) {
            tr.hide();
            tr = tr.next();
        }
    });
}

function db_collapseAllModules() {
    db_collapseModules($$("a.moduleName"));
}

function db_init() {
    $$("input.isMonolithic").each(function(box) {
        db_showTestType(box);
    });
    db_collapseAllModules();
}