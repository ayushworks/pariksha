package pariksha

import cats.effect.Sync
import pariksha.RuleResult.{Failed, Passed}
import pariksha.ValidationResult.{Invalid, Valid}
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}
import cats._

/**
  * @author Ayush Mittal
  */
object ResultUtil {

  def fromRuleResults[T](value: T, result: List[RuleResult[T]]): ValidationResult = result.collect { case Failed(errors) => errors } match {
      case Nil => Valid(value)
      case errors => Invalid(value, errors.flatten)
    }


  def fromValidationListAsync[T](value: T, validations: List[Validation[T]])(implicit executionContext: ExecutionContext): Future[ValidationResult] = Future.sequence(validations.map {
      validation =>
        Future(validation.check(value))
    }).map(fromRuleResults(value, _))


  def catsValidation[T, F[_]](value: T, validations: List[Validation[T]])(implicit p: Parallel[F], f: Sync[F]) : F[ValidationResult] = {
    validations.map{
      validation =>
        f.delay(validation.check(value))
    }.parSequence.map(fromRuleResults(value, _))
  }

  def fromValidationList[T](value: T, validations: List[Validation[T]]): ValidationResult = {
    var result : RuleResult[T] = Passed(value)
    validations.collectFirst { case validation if {
      result = validation.check(value)
      !result.isSuccess
    } =>
      Invalid(value, result.getErrors)
    }.getOrElse(Valid(value))
  }
}
