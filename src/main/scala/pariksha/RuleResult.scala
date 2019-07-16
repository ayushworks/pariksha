package pariksha

/**
 * @author Ayush Mittal
 */
sealed trait RuleResult

object RuleResult {
  case class Passed[T](value: T) extends RuleResult
  case class Failed(errors: List[ValidationError]) extends RuleResult
}
