package pariksha

import org.scalatest.{Matchers, WordSpec}
import pariksha.models.{TVCharacter, Values}

/**
  * @author Ayush Mittal
  */
class ValidateNotNullSpec extends WordSpec with Matchers with Values {

  import syntax._

  "Not null validation" when {

    "there are simple case classes" should {

      "validate TVCharacter for not null rule" in {
        invalidCharacterAllNull.validate(TVCharacter.notNullValidations).errors shouldBe List(
          ValidationError("TVCharacter.'name' must not be null"),
          ValidationError("TVCharacter.'showName' must not be null")
        )

        emptyCharacter.validate(TVCharacter.notNullValidations).isValid shouldBe true

        validCharacter.validate(TVCharacter.notNullValidations).isValid shouldBe true

      }

      "validate TVCharacter for all rules" in {
        invalidCharacterAllNull.validate(TVCharacter.allValidations).errors shouldBe List(
          ValidationError("TVCharacter.'name' must not be null"),
          ValidationError("TVCharacter.'showName' must not be null"),
          ValidationError(TVCharacter.nameNonEmptyMsg),
          ValidationError(TVCharacter.showNameNonEmptyMsg)
        )

        emptyCharacter.validate(TVCharacter.allValidations).errors shouldBe List(
          ValidationError(TVCharacter.nameNonEmptyMsg),
          ValidationError(TVCharacter.showNameNonEmptyMsg)
        )

        validCharacter.validate(TVCharacter.allValidations).isValid shouldBe true
      }
    }

    "there are composed case classes" should {

      "validate Artist" in {

        nullArtist.validate.errors shouldBe List(
          ValidationError("Artist.'name' must not be null"),
          ValidationError("Artist.'character' must not be null"),
          ValidationError(TVCharacter.nameNonEmptyMsg),
          ValidationError(TVCharacter.showNameNonEmptyMsg)
        )

        tvCharacterNullArtist.validate.errors shouldBe List(
          ValidationError("TVCharacter.'name' must not be null"),
          ValidationError("TVCharacter.'showName' must not be null"),
          ValidationError(TVCharacter.nameNonEmptyMsg),
          ValidationError(TVCharacter.showNameNonEmptyMsg)
        )

        tvCharacterEmptyArtist.validate.errors shouldBe List(
          ValidationError(TVCharacter.nameNonEmptyMsg),
          ValidationError(TVCharacter.showNameNonEmptyMsg)
        )

        validArtist.validate.isValid shouldBe true
      }
    }
  }
}
