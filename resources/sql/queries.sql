-- :name create-ingredient! :! :n
-- :doc creates a new ingredient record
INSERT INTO ingredients
(name, created_at, updated_at)
VALUES (:name, now(), now())

-- :name get-ingredient :? :1
-- :doc retrieves an ingredient by its id
SELECT * FROM ingredients
WHERE id = :id::uuid

-- :name get-ingredients :? :*
-- :doc retrieves all ingredients
SELECT * FROM ingredients
