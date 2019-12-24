package pariksha.helpers

import cats.effect.IO
import org.scalatest.concurrent.ScalaFutures
import pariksha.{ValidationResult, Validator, syntax}
import scala.concurrent.ExecutionContext

/**
  * @author Ayush Mittal
  */
object TestHelpers extends ScalaFutures {

  implicit class ValidationTestOps[T](t: T) {

    import syntax._

    def validateAsyncF(implicit validator: Validator[T], executionContext: ExecutionContext): ValidationResult = {
      t.validateAsync.futureValue
    }

    def validateIO(implicit validator: Validator[T], executionContext: ExecutionContext): ValidationResult = {
      implicit val contextShift = IO.contextShift(executionContext)
      t.validateF[IO].unsafeToFuture().futureValue
    }
  }

}
