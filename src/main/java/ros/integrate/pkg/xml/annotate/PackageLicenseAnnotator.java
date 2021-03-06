package ros.integrate.pkg.xml.annotate;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import ros.integrate.pkg.xml.TagTextRange;
import org.jetbrains.annotations.NotNull;
import ros.integrate.pkg.xml.ROSPackageXml;
import ros.integrate.pkg.xml.ROSPackageXml.License;
import ros.integrate.pkg.xml.intention.AddLicenceQuickFix;
import ros.integrate.pkg.xml.intention.FixLicenseQuickFix;
import ros.integrate.pkg.xml.intention.RemoveLicenseQuickFix;
import ros.integrate.pkg.xml.intention.SplitLicenseQuickFix;

import java.util.List;

class PackageLicenseAnnotator {
    @NotNull
    private final ROSPackageXml pkgXml;
    @NotNull
    private final AnnotationHolder holder;
    @NotNull
    private final List<License> licenses;
    @NotNull
    private final List<TagTextRange> licenseTrs;

    PackageLicenseAnnotator(@NotNull ROSPackageXml pkgXml, @NotNull AnnotationHolder holder) {
        this.pkgXml = pkgXml;
        this.holder = holder;
        licenses = pkgXml.getLicences();
        licenseTrs = pkgXml.getLicenceTextRanges();
    }

    void annNoLicenses() {
        if (licenses.isEmpty()) {
            Annotation ann = holder.createErrorAnnotation(licenseTrs.get(0),
                    "Package must have at least one licence.");
            ann.registerFix(new AddLicenceQuickFix(pkgXml));
        }
    }

    void annBadLicenses() {
        if (licenses.isEmpty()) {
            return;
        }
        for (int i = 0; i < licenses.size(); i++) {
            License license = licenses.get(i);
            TagTextRange tr = licenseTrs.get(i);
            if (license.getValue().isEmpty()) {
                Annotation ann = holder.createErrorAnnotation(tr,
                        "License tags cannot be empty.");
                ann.registerFix(new FixLicenseQuickFix(pkgXml, i));
                if (licenses.size() > 1) {
                    ann.registerFix(new RemoveLicenseQuickFix(pkgXml, i));
                }
            } else if (license.getValue().contains(",")) {
                Annotation ann = holder.createWarningAnnotation(tr.value(),
                        "Each license tag must only hold ONE license.");
                ann.registerFix(new SplitLicenseQuickFix(pkgXml, i));
            }
        }
    }

    void annTodoLicense() {
        if (licenses.isEmpty()) {
            return;
        }
        for (int i = 0; i < licenses.size(); i++) {
            License license = licenses.get(i);
            TagTextRange tr = licenseTrs.get(i);
            if (license.getValue().matches("TODO")) {
                Annotation ann = holder.createWeakWarningAnnotation(tr.value(),
                        "This only acts a placeholder for an actual license, please choose one");
                if (licenses.size() > 1) {
                    ann.registerFix(new RemoveLicenseQuickFix(pkgXml, i));
                }
            }
        }
    }
}
