INSERT INTO books (id, title, author, category, price, publication_year, quantity) VALUES
(1, 'Clean Code', 'Robert C. Martin', 'Software Engineering', 45.90, 2008, 10),
(2, 'Effective Java', 'Joshua Bloch', 'Software Engineering', 55.00, 2018, 8),
(3, 'Domain-Driven Design', 'Eric Evans', 'Software Engineering', 60.50, 2003, 5),
(4, 'Refactoring', 'Martin Fowler', 'Software Engineering', 50.00, 1999, 7),
(5, 'The Pragmatic Programmer', 'Andrew Hunt, David Thomas', 'Software Engineering', 42.75, 1999, 9),
(6, 'Clean Architecture', 'Robert C. Martin', 'Software Engineering', 48.20, 2017, 6),
(7, 'Test-Driven Development', 'Kent Beck', 'Software Engineering', 38.99, 2002, 4),
(8, 'Working Effectively with Legacy Code', 'Michael Feathers', 'Software Engineering', 52.30, 2004, 3),
(9, 'Patterns of Enterprise Application Architecture', 'Martin Fowler', 'Software Engineering', 58.10, 2002, 2),
(10, 'Head First Design Patterns', 'Eric Freeman, Elisabeth Robson', 'Software Engineering', 45.00, 2004, 5);

-- Ensure the identity generator now starts AFTER 10
ALTER TABLE books ALTER COLUMN id RESTART WITH 11;