insert into CATEGORY
  (id, category_name )
    values
      (1, 'cars'),
      (2, 'trucks'),
      (3, 'buses'),
      (4, 'mototechnics'),
      (5, 'special technics'),
      (6, 'agriculture machinery'),
      (7, 'trailers and semi-trailers'),
      (8, 'water transport');

insert into CATEGORY
  (id, category_name, parent_id)
    values
      (9, 'audi', 1),
      (10, 'mersedes', 1),
      (11, 'volvo', 1),
      (12, 'opel', 1),
      (13, 'A8', 9),
      (14, 'A4', 9),
      (15, 'S8', 9),
      (17, 'E200', 10),
      (18, 'volkswagen', 3);