package pariksha

/**
 * @author Ayush Mittal
 */
trait RuleResult[+T] {

  def value: T

  def errors: List[ValidationError]

  def isValid: Boolean
}

case class RulePass[T](val value: T) extends RuleResult[T] {

  override def errors: List[ValidationError] = throw new NoSuchElementException

  override def isValid: Boolean = true
}

case class RuleFailed(val errors: List[ValidationError]) extends RuleResult[Nothing] {

  override def value: Nothing = throw new NoSuchElementException

  override def isValid: Boolean = false
}
