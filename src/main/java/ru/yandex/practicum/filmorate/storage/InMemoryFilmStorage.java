package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidCountException;
import ru.yandex.practicum.filmorate.exceptions.invalidFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    public static final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    @Override
    public Film create(Film film) {
        Film NewFilm = new Film(id, film.getName(),film.getDescription(),film.getReleaseDate(),film.getDuration(), film.getMpa(),film.getGenres());
        films.put(film.getId(), NewFilm);
        id++;
        log.info("field with type {} and id={} added.", film.getClass(), film.getId());
        return films.get(NewFilm.getId());
    }

    @Override
    public Film update(Film film) {
        for (Film x : films.values()) {
            if (x.getId() == film.getId()) {
                x.setName(film.getName());
                x.setDescription(film.getDescription());
                x.setReleaseDate(film.getReleaseDate());
                x.setDuration(film.getDuration());
            }
        }
        log.info("field  with id={} updated: {}", film.getId(), film.getClass());
        return films.get(film.getId());
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
        log.info("field  with id={} deleted: {}", film.getId(), film.getClass());
    }

    @Override
    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new invalidFilmIdException("фильм" + id);
        }
        return films.get(id);
    }

    @Override
    public Integer addLike(Integer filmId, Integer userId) {
        if (id <= 0) {
            throw new invalidFilmIdException("The user ID cannot be negative.");
        }
        if (userId <= 0) {
            throw new invalidFilmIdException("The user ID cannot be negative.");
        }

        log.info("field  with id={} liked by: {}", id, userId);
        films.get(filmId).getLikes().add(userId);
        return userId;
    }


    @Override
    public Integer removeLike(Integer filmId, Integer userId) {
        if (id <= 0) {
            throw new invalidFilmIdException("The user ID cannot be negative.");
        }
        if (userId <= 0) {
            throw new invalidFilmIdException("The user ID cannot be negative.");
        }
        films.get(filmId).getLikes().remove(userId);
        log.info("field  with id={} unliked by: {}", id, userId);
        return userId;
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        if (count <= 0) {
            throw new InvalidCountException("The count cannot be negative.");
        }
        List<Film> unsortedMap = new ArrayList<>(films.values());
        List<Film> topTen = unsortedMap.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count).collect(Collectors.toList());
        log.info("field count={} best films", count);
        return topTen;
    }


    @Override
    public List<Film> list() {
        List<Film> list = new ArrayList<>();
        for (Film x : films.values()) {
            list.add(x);
        }
        log.info("number of films {}:", films.size());
        return list;
    }
}
