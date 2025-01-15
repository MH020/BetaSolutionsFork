
-- Drop existing tables if they exist
DROP TABLE IF EXISTS project_employee_task_subTask CASCADE;
DROP TABLE IF EXISTS project_employee_task CASCADE;
DROP TABLE IF EXISTS project_employee CASCADE;
DROP TABLE IF EXISTS profile CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS sub_task CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS sub_project CASCADE;
DROP TABLE IF EXISTS project CASCADE;

-- Create Tables
CREATE TABLE IF NOT EXISTS project (
                                       project_id INT PRIMARY KEY AUTO_INCREMENT,
                                       project_name VARCHAR(100) NOT NULL,
                                       project_owner VARCHAR(100) NOT NULL,
                                       project_total_hours INT NOT NULL,
                                       project_total_days INT NOT NULL,
                                       project_total_price DOUBLE NOT NULL,
                                       project_deadline DATE NOT NULL,
                                       project_start_date DATE NOT NULL,
                                       project_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS sub_project (
                                           sub_project_id INT PRIMARY KEY AUTO_INCREMENT,
                                           sub_project_name VARCHAR(100) NOT NULL,
                                           sub_project_total_hours INT NOT NULL,
                                           sub_project_total_days INT NOT NULL,
                                           sub_project_total_price DOUBLE NOT NULL,
                                           sub_project_deadline DATE NOT NULL,
                                           sub_project_start_date DATE NOT NULL,
                                           project_id INT NOT NULL,
                                           sub_project_active BOOLEAN NOT NULL DEFAULT TRUE,
                                           FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task (
                                    task_id INT PRIMARY KEY AUTO_INCREMENT,
                                    task_name VARCHAR(100) NOT NULL,
                                    task_total_hours INT NOT NULL,
                                    task_hours INT NOT NULL,
                                    task_total_days INT NOT NULL,
                                    task_days INT NOT NULL,
                                    task_total_price DOUBLE NOT NULL,
                                    task_deadline DATE NOT NULL,
                                    task_start_date DATE NOT NULL,
                                    sub_project_id INT,
                                    task_active BOOLEAN NOT NULL DEFAULT TRUE,
                                    project_id INT NOT NULL,
                                    FOREIGN KEY (sub_project_id) REFERENCES sub_project (sub_project_id) ON DELETE CASCADE,
                                    FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sub_task (
                                        sub_task_id INT PRIMARY KEY AUTO_INCREMENT,
                                        sub_task_name VARCHAR(100) NOT NULL,
                                        sub_task_total_hours INT NOT NULL,
                                        sub_task_total_days INT NOT NULL,
                                        sub_task_total_price DOUBLE NOT NULL,
                                        sub_task_deadline DATE NOT NULL,
                                        sub_task_start_date DATE NOT NULL,
                                        task_id INT NOT NULL,
                                        sub_task_active BOOLEAN NOT NULL DEFAULT TRUE,
                                        FOREIGN KEY (task_id) REFERENCES task (task_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employee (
                                        employee_id INT PRIMARY KEY AUTO_INCREMENT,
                                        employee_name VARCHAR(100) NOT NULL,
                                        employee_office VARCHAR(100) NOT NULL,
                                        employee_proficiency VARCHAR(100) NOT NULL,
                                        employee_salary DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS profile (
                                       login_id INT PRIMARY KEY AUTO_INCREMENT,
                                       password VARCHAR(50) NOT NULL,
                                       email VARCHAR(50) NOT NULL,
                                       employee_id INT NOT NULL,
                                       FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS project_employee (
                                                employee_id INT NOT NULL,
                                                project_id INT NOT NULL,
                                                PRIMARY KEY (employee_id, project_id),
                                                FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE,
                                                FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS project_employee_task (
                                                     employee_id INT NOT NULL,
                                                     project_id INT NOT NULL,
                                                     task_id INT NOT NULL,
                                                     PRIMARY KEY (employee_id, project_id, task_id),
                                                     FOREIGN KEY (employee_id, project_id) REFERENCES project_employee (employee_id, project_id) ON DELETE CASCADE,
                                                     FOREIGN KEY (task_id) REFERENCES task (task_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS project_employee_task_subTask (
                                                             employee_id INT NOT NULL,
                                                             project_id INT NOT NULL,
                                                             task_id INT NOT NULL,
                                                             sub_task_id INT NOT NULL,
                                                             PRIMARY KEY (employee_id, project_id, task_id, sub_task_id),
                                                             FOREIGN KEY (employee_id, project_id, task_id) REFERENCES project_employee_task (employee_id, project_id, task_id) ON DELETE CASCADE,
                                                             FOREIGN KEY (sub_task_id) REFERENCES sub_task (sub_task_id) ON DELETE CASCADE
);
-- Insert sample data into project table
INSERT INTO project (project_name, project_owner, project_total_hours, project_total_days, project_total_price, project_deadline, project_start_date, project_active)
VALUES
    ('Car dealership', 'Suzan', 500, 30, 10000.0, '2025-01-31', '2024-11-01', TRUE),
    ('Pizza system', 'Mads', 300, 20, 8000.0, '2025-01-15', '2024-12-01', TRUE);
-- Insert sample data into sub_project table
INSERT INTO sub_project (sub_project_name, sub_project_total_hours, sub_project_total_days, sub_project_total_price, sub_project_deadline, sub_project_start_date, project_id, sub_project_active)
VALUES
    ('Rent a car', 200, 15, 5000.0, '2024-12-15', '2024-11-05', 1, TRUE),
    ('Create menu', 100, 10, 3000.0, '2025-01-05', '2024-12-10', 2, TRUE);

-- Insert sample data into task table
INSERT INTO task (task_name, task_total_hours, task_hours, task_total_days,task_days, task_total_price, task_deadline, task_start_date, project_id, sub_project_id, task_active)
VALUES
    ('Select a car', 100, 50, 10, 5, 2000.0, '2024-12-10', '2024-11-01', 1, 1, TRUE),
    ('Create a pizza', 50, 40, 5, 4, 1000.0, '2024-12-25', '2024-11-10', 2, 2, TRUE);

-- Insert sample data into sub_task table
INSERT INTO sub_task (sub_task_name, sub_task_total_hours, sub_task_total_days, sub_task_total_price, sub_task_deadline, sub_task_start_date, task_id, sub_task_active)
VALUES
    ('Insert car information', 20, 3, 500.0, '2024-12-03', '2024-11-10', 1, TRUE),
    ('Insert pizza information', 10, 2, 300.0, '2024-12-18', '2024-11-15', 2, TRUE);

-- Insert sample data into employee table
INSERT INTO employee (employee_name, employee_office, employee_proficiency, employee_salary)
VALUES
    ('Mads', 'Copenhagen', 'Manager', 6000.0),

    ('Oscar', 'New Jersey', 'Tester', 3000.0),

    ('Lucy', 'Oslo', 'Designer', 4000.0),

    ('John', 'Dallas', 'Manager', 6000.0),

    ('Lauren', 'Los Angeles', 'Developer', 5500.0);

-- Insert sample data into profile table
INSERT INTO profile (password, email, employee_id)
VALUES
    ('password1', 'employee1@example.com', 1),
    ('password2', 'employee2@example.com', 2);

-- Insert sample data into project_employee table
INSERT INTO project_employee (employee_id, project_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2),
    (4, 2);


-- Insert sample data into project_employee_task table
INSERT INTO project_employee_task (employee_id, project_id, task_id)
VALUES
    (1, 1, 1),
    (2, 2, 2),
    (4, 2, 2);

-- Insert sample data into project_employee_task_subTask table
INSERT INTO project_employee_task_subTask (employee_id, project_id, task_id, sub_task_id)
VALUES
    (1, 1, 1, 1),
    (2, 2, 2, 2);



