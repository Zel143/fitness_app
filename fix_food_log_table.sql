-- Fix food_log table to make food_library_id nullable
-- Run this SQL script in MySQL Workbench or command line

USE fittrack_db;

-- Drop the existing foreign key constraint
ALTER TABLE food_log 
DROP FOREIGN KEY food_log_ibfk_2;

-- Modify the column to be nullable
ALTER TABLE food_log 
MODIFY COLUMN food_library_id INT NULL;

-- Add the foreign key constraint back with ON DELETE SET NULL
ALTER TABLE food_log 
ADD CONSTRAINT food_log_ibfk_2 
FOREIGN KEY (food_library_id) 
REFERENCES food_library(food_id) 
ON DELETE SET NULL;

-- Verify the changes
DESCRIBE food_log;

SELECT 'Food log table has been successfully updated!' AS Status;
