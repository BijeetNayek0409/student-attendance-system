DROP TABLE IF EXISTS public.attendance;
DROP TABLE IF EXISTS public.students;

CREATE TABLE public.students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    class VARCHAR(50)
);

CREATE TABLE public.attendance (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES public.students(id),
    date DATE,
    status VARCHAR(10)
);
SELECT * FROM public.students;
INSERT INTO public.students (id, name, class) VALUES
(81, 'Bijeet', 'B1'),
(85, 'Vini', 'B1'),
(72, 'Mayank', 'B1'),
(99, 'Shantanu', 'B1'),
(82, 'Afraz', 'B1'),
(89, 'Panda', 'B1'),
(83, 'Divy', 'B1');

select * from public.attendance;