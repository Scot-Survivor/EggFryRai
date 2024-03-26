# Brief Overview of Tests
Current test passing right is `41/41 | 100%`.

Our tests cover `93% (60/64)` of classes, `75% (251/332)` of methods  
and `76% (1120/1465)` of lines. (Data correct As of 22/03/2024).

All tests inherit from the "SetupTests" class that runs before each test.
This ensures the config is pointing to the "testing" config file which allows for
reproducibility of tests.

# Entity Validator Tests
## ChronologicalDates Validator
### testStartDateBeforeEndDate
This test checks if the start date is before the end date.
And that the entity passes if this is the case.
### testStartDateAfterEndDate
This test ensures that the entity fails if the start date is after the end date.
### testStartDateEqualsEndDate
This ensures the entity fails if the two dates are equal.

## InFuture Validator
### testBookingSucceedsOnFutureDateTime
This ensures that the date passed in booking is in the future,
and more importantly that the entity is valid if this is the case.
### testBookingFailsOnPastDateTime
Opposite of the first test, this checks the entity fails validation
if the date is in the past.

## MFA Validator
### test2FAFailsOnMissingValues
This checks the entity fails validation if 2fa is enabled but
the secret or recovery key is missing.
### test2FAPassesWith2FA
This checks the entity passes validation if 2fa is enabled and
both the secret and recovery key are present.
### test2FAPassesWithout2FA
This checks the entity passes validation if 2fa is not enabled.

# Integration Tests
## LoginScreen Tests
### Notes
All tests use:
- `example@example.com` or `mfa@example.com` for email
- and `password` or `INVALID_PASSWORD`
- In the future these tests will be changed to mock the database,
  rather than relying on a testing in memory database (configured in dev-hibernate)
### testScreenHasLoginButton
This checks the login screen has a login button.
### testScreenHasPasswordField
This checks the login screen has a password field.
### testSuccessfulLoginNoMFA
This checks that a user can login successfully without 2fa.
### testSuccessfulLoginMFARecoveryCode
This checks that a user can login successfully with 2fa using a recovery code.
### testSuccessfulLoginMFAInvalidRecoveryCode
This checks that a user cannot login with an invalid recovery code.
### testFailedLogin
This checks that a user cannot login with invalid credentials.

## Generic ScreenTests
### testAllScreensWereRegistered
This test uses reflections to ensure that all screens in the screens package
were registered with the screen manager.
### testScreenGoBack
Test the screen manager can go back to the previous screen.
### testThatLoginScreenDisplaysFirst
Test that the login screen is the first screen displayed.

## RegisterTest
### testThatAUserCanRegister
This test checks that a user can register successfully.
### testThatAUserCanLoginAfterRegister
This test checks that a user can login after registering.
### testThatAUserCannotRegisterWithInvalidEmail
This test checks that a user cannot register with an invalid email.

# Basic Tests
## ConfigTests
### Notes
- The testing config is found in test/resources
### BeforeAll
this is a method that runs when the class first starts, this loads the testing config
via Apache Commons (This is not tested as we assume libraries work as intended), to check
the values against the AppConfig class.
### testConfigFileLoaded
First check the config file was even loaded
### testAllPropertiesLoaded
This checks all properties found in the config are found in the class (as public static fields)
### testPropertySetViaMethod
This checks a property can be changed via the classes method(s).
### testCleanConfigMethod
This checks the clean method removes unknown or old values from a saved config.
This clean method logs a warning upon running.

## Database Tests
### Notes
- These are generic database tests not associated with any specific entity.
### testDatabaseConnection
Check a connection to the database has been made.

## Event Tests
### Notes
- Events while not specifically wanted by the brief, allow us as the developers to add on to
  existing classes and methods without having to create children to those. For example the
  validator listener that applies validation checks to entities, at the database method call time.

### testAllEventsHave0ArgConstructor
All Events must have a 0 Arg constructor in order to be tested, this ensures this is the case
and provides more readable errors to developers if an event does not have this.

### testListenerGetsInvoked
This ensures that the listener is invoked when an expected event is fired.

### testAllEventTypes
This checks every single event is fired by listening for the "GenericEvent",
which all events inherit from.

### testAllEventsHaveAppropriateListenerMethods
This ensures for every test there is a listener method that listens for the event.
For example "LoginEvent" should have "onLogin", the reflections used to find the listener methods
have this style hard coded, this test provides developer readable error if this is not the case.

## Insurance Tests
### Notes
- Tests for insurance entity
### testInsuranceEntityCreation
Tests entity is successfully created

## PasswordTests
### Notes
- This is a testing class used to check the behaviour of the Password Hashers.
- This test will be changed in the future to rely less on hard coded tests and use
  reflections to find the different implementations of PasswordManager
### testPasswordInstanceOfArgon
This checks we get an instance of the Argon password manager if we pass "Argon" to the factory.
### testPasswordInstanceOfBcrypt
Same as Argon expect for BCrypt
### testPasswordThrowsIllegalArgumentException
This ensures a factory throws an Illegal Argument Exception if an unknown password manager is passed.

### testArgonPasswordValid
Tests that two of the same passwords hashed with Argon get verified correctly.
"password" and "password" are used for simplicity.

### testArgonPasswordInvalid
Reverse of the earlier test checks "password" and "password1" do not verify correctly.

### testBCryptPasswordValid
Same as Argon test but for BCrypt.

### testBCryptPasswordInvalid
Same as Argon test but for BCrypt.

## BookingTests
### testBookingCreation
This test checks that a booking can be created successfully.

## RoomTests
### testRoomCreation
This test checks that a room can be created successfully.