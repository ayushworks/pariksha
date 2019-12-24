package pariksha

import cats.Parallel
import cats.effect.Sync
import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Ayush Mittal
 */
trait Validator[T] {

  val validations: List[Validation[T]]

  def validate(obj: T): ValidationResult = {
    ValidationResult(obj, validations.map(_.check(obj)))
  }

  def validateFailFast(obj: T): ValidationResult = {
    ValidationResult.failFast(obj, validations)
  }

  def validateAsync(obj: T)(implicit executionContext: ExecutionContext): Future[ValidationResult] = {
    ValidationResult.validateAsync(obj, validations)
  }

  def validateF[F[_]](obj: T)(implicit p: Parallel[F], f: Sync[F]) : F[ValidationResult] = {
    ValidationResult.validateF(obj, validations)
  }
}

object Validator {

  def apply[T]: Validator[T] = new Validator[T] {
    val validations: List[Validation[T]] = Nil
  }

  def apply[T](validationList: List[Validation[T]]): Validator[T] = new Validator[T] {
    override val validations: List[Validation[T]] = validationList
  }
}
