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

  # ---------------------------------------------------------------------------
  Scenario: Get movie by ID for Inception
    Given path 'movies', 1
    When method get
    Then status 200
    And match response.title == 'Inception'
    And match response.director == 'Christopher Nolan'
    And match response.releaseDate == 2010

  # ---------------------------------------------------------------------------
  Scenario: Create new movie - The Prestige
    Given path 'movies'
    And request
      """
      {
        "title": "The Prestige",
        "director": "Christopher Nolan",
        "releaseDate": 2006
      }
      """
    When method post
    Then status 201
    And match response.statusCode == '201 '
    And match response.statusMsg == 'Movie Created Successfully'

  # ---------------------------------------------------------------------------
  Scenario: Find movies by director
    Given path 'movies/director'
    And param director = 'Christopher Nolan'
    When method get
    Then status 200
    And match each response[*].director == 'Christopher Nolan'

  # ---------------------------------------------------------------------------
  Scenario: Get movie by ID that does not exist
    Given path 'movies', 999
    When method get
    Then status 404


