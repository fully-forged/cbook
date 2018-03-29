-- :name create-ingredient! :<! :1
-- :doc creates a new ingredient record
INSERT INTO ingredients
(name, created_at, updated_at)
VALUES (:name, now(), now())
RETURNING id

-- :name get-ingredient :? :1
-- :doc retrieves an ingredient by its id
SELECT :i*:cols FROM ingredients
WHERE id = :id::uuid

-- :name get-ingredients :? :*
-- :doc retrieves all ingredients
SELECT :i*:cols FROM ingredients
