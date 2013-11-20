package fr.norad.jaxrs.oauth2;

import java.lang.annotation.Annotation;
import lombok.Getter;

public class Secured {
    Annotation processed;
    @Getter
    private String[] scopes;
    @Getter
    private boolean andAssociation;
    @Getter
    private boolean notSecured;

    void read(NotSecured notSecured) {
        checkNotFilled(notSecured);
        this.notSecured = notSecured != null;
    }

    void read(SecuredWithScope scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = new String[]{scope.value()};
        }
    }

    void read(SecuredWithAllScopesOf scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = scope.value();
            andAssociation = true;
        }

    }

    void read(SecuredWithAnyScopesOf scope) {
        checkNotFilled(scope);
        if (scope != null) {
            scopes = scope.value();
            andAssociation = false;
        }
    }

    private void checkNotFilled(Annotation a) {
        if (a == null) {
            return;
        }
        if (processed != null) {
            throw new IllegalStateException("only one accept annotation is allowed. found " + a + " and "
                                                    + processed);
        }
        processed = a;
    }
}
