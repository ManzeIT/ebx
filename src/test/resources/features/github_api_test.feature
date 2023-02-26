Feature: Test GITHUB Auth Token

Scenario: Validate github access through a valid auth token
  Given a valid auth token is used
  When requesting the latest commits into master of repo MediatR and username:jbogard
  Then the most recent commit is authored by Jimmy Bogard and the date is 2023-02-23

  Scenario: Invalid auth token trying to access github API
    Given an invalid auth token is used
    When requesting the latest commits into master of repo MediatR and username:jbogard
    Then the Bad credentials error message is in the response

