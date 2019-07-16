package pariksha

/**
 * @author Ayush Mittal
 */
sealed trait RuleResult[+T] {

  def getValue: T

  def getErrors: List[ValidationError]

  def isSuccess : Boolean
}

object RuleResult {

  case class Passed[T](value: T) extends RuleResult[T] {
    override def getErrors: List[ValidationError] = throw new NoSuchElementException

    override def getValue: T = value

    override def isSuccess: Boolean = true
  }

  case class Failed(errors: List[ValidationError]) extends RuleResult[Nothing] {
    override def getValue: Nothing = throw new NoSuchElementException

    override def getErrors: List[ValidationError] = errors

    override def isSuccess: Boolean = false
  }
}
