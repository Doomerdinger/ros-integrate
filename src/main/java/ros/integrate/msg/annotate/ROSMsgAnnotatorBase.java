package ros.integrate.msg.annotate;

import com.intellij.lang.annotation.AnnotationHolder;
import org.jetbrains.annotations.NotNull;

/**
 * a template class for ROS message annotators.
 *
 * Annotators are split based on what element they mark if something goes wrong.
 * The object created by annotators is known as an annotation.
 */
abstract class ROSMsgAnnotatorBase {
    final @NotNull AnnotationHolder holder;

    ROSMsgAnnotatorBase(@NotNull AnnotationHolder holder) {
        this.holder = holder;
    }
}
