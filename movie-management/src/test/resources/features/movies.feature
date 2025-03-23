Feature: Movie API Integration Tests


  Background:
    * url baseUrl

  # ---------------------------------------------------------------------------
  Scenario: Get all movies using pre-loaded data
    Given path 'movies'
    When method get
    Then status 200
    And match response[*].title contains "Inception"
    And match response[*].title contains "The Dark Knight"
    And match response[*].title contains "Interstellar"
    And match response[*].title contains "Dunkirk"

  # ---------------------------------------------------------------------------
  Scenario: Get movie by ID for Inception
    Given path 'movies', 1
    When method get
    Then status 200
    And match response.title == 'Inception'
    And match response.director == 'Christopher Nolan'
    And match response.releaseDate == 2010

  # ---------------------------------------------------------------------------
  Scenario: Update movie for Interstellar
    Given path 'movies', 3
    And request { title: 'Interstellar Updated', director: 'Christopher Nolan', releaseDate: 2014 }
    When method put
    Then status 201
    And match response.statusCode == '201 '
    And match response.statusMsg == 'Movie updated Successfully'

  # ---------------------------------------------------------------------------
  Scenario: Delete movie for Dunkirk
    Given path 'movies', 4
    When method delete
    Then status 201
    And match response.statusCode == '201 '
    And match response.statusMsg == 'Movie has been deleted successfully'


