package pariksha

import pariksha.ValidationResult.{Invalid, Valid}

import scala.collection.GenTraversable
import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Ayush Mittal
 */
object syntax {

  implicit class ValidationsOps[T](t: T) {

    def validate(implicit validator: Validator[T]): ValidationResult =
      validator.validate(t)

    def validateFailFast(implicit validator: Validator[T]): ValidationResult =
      validator.validateFailFast(t)

  }

  implicit class ValidationOps[T](t: T) {

    def validateAsync(implicit validator: Validator[T], executionContext: ExecutionContext): Future[ValidationResult] =
      validator.validateAsync(t)
  }

  implicit class ValidationsTraversableOps[T](ts: GenTraversable[T]) {

    def validate(implicit validator: Validator[T]): Traversable[ValidationResult] =
      ts.seq.map {
        t =>
          validator.validate(t)
      }
  }

  implicit class ValidationResultOps(result: ValidationResult) {
    def isValid: Boolean = result match {
      case Valid(_) => true
      case _ => false
    }

    def errors: List[ValidationError] = result match {
      case Valid(_) => Nil
      case Invalid(_, errors) => errors
    }
  }
}
