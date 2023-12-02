Feature: ProductServlet

  Scenario: User creates a new product
    Given the correct product request:
      | name            | Nowy Produkt            |
      | price           | 1000                    |
      | amount          | 100                     |
      | description     | Opis Produktu           |
      | productCategory | EXAMPLE                 |
      | image           | /path/to/image/base64   |

    When the user creates the product
    Then the product should be created successfully
    And the response should contain the product details

  Scenario: User tries to create product with incorrect price
    Given the incorrect price product request:
      | name            | Nowy Produkt            |
      | price           | -100                    |
      | amount          | 100                     |
      | description     | Opis Produktu           |
      | productCategory | EXAMPLE                 |
      | image           | /path/to/image/base64   |

    When user tries to create product
    Then api should respond with bad request
    And the response should contain error message

  Scenario: User updates product with given request
    Given product with amount count 0:
      | name            | Nowy Produkt            |
      | price           | 100                     |
      | amount          | 0                       |
      | description     | Opis Produktu           |
      | productCategory | EXAMPLE                 |
      | image           | /path/to/image/base64   |

    When user updates product
    Then api should respond with updated product
    And the updated product availability field should be false

  Scenario: User requests for product changelog
    Given the following existing product data:
      | name            | Existing Product               |
      | price           | 80                             |
      | amount          | 5                              |
      | description     | Existing Product Desc          |
      | productCategory | EXAMPLE                        |
      | image           | /path/to/existing/image/base64 |

    When the user updates the product with the following changes:
      | name            | Updated Product               |
      | price           | 100                           |
      | amount          | 0                             |
      | description     | Updated Product Desc          |
      | productCategory | EXAMPLE                       |
      | image           | /path/to/updated/image/base64 |

    And the user requests for updated product changelog
    Then the response should contain the data before the update:
      | name            | Existing Product               |
      | price           | 80                             |
      | amount          | 5                              |
      | description     | Existing Product Desc          |
      | productCategory | EXAMPLE                        |
      | image           | /path/to/existing/image/base64 |