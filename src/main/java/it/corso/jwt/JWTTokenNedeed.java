package it.corso.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.ws.rs.NameBinding;

@NameBinding // -> indica che la tipologia di annotation personalizzata può essere utilizzata
				// come una annotation di legatura di un elemento di Jax-rs (può essere attacata
				// ai metodi che ricevono la request come il controller)
@Retention(RetentionPolicy.RUNTIME) // -> quando deve girare questo filtro? in runtime
@Target({ ElementType.TYPE, ElementType.METHOD }) // -> facciamo in modo che possiamo mettere sopra classi,interfacce e
													// metodi
public @interface JWTTokenNedeed {

}
