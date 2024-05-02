package it.corso.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME) // dovrà essere convertita in fase di runtime
@Target({ ElementType.TYPE, ElementType.METHOD }) // tipi a cui è possibile applicare questa annotation					
public @interface Secured {
	String role() default "all"; // definizione membro role dentro l'annotation 
}
