package pariksha.helpers

import org.scalatest.concurrent.ScalaFutures
import pariksha.{ValidationResult, Validator}

import scala.concurrent.ExecutionContext

/**
  * @author Ayush Mittal
  */
object FutureHelper extends ScalaFutures {

  implicit class ValidationFutureTestOps[T](t: T) {

    def validateAsyncF(implicit validator: Validator[T], executionContext: ExecutionContext): ValidationResult =
      validator.validateAsync(t).futureValue
  }

}
