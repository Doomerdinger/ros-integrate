package ros.integrate.msg.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * a generic inline identifier in ROS messages.
 */
public interface ROSMsgIdentifier extends PsiNameIdentifierOwner {
    PsiElement set(String newName);
}
