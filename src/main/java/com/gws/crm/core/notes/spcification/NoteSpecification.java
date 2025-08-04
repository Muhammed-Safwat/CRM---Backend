package com.gws.crm.core.notes.spcification;

import com.gws.crm.core.notes.dtos.NoteCriteria;
import com.gws.crm.core.notes.entity.Note;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteSpecification {

    public static Specification<Note> filter(NoteCriteria criteria,
                                             Long createdId) {
        List<Specification<Note>> specs = new ArrayList<>();

        if (criteria != null) {
            specs.add(byTargetId(criteria.getTargetId()));
            specs.add(byCreatedAt(criteria.getCreatedAt()));
            specs.add(byNoteType(criteria.getNoteType()));
            specs.add(byNoteTypeId(criteria.getNoteTypeId()));
            specs.add(byKeyword(criteria.getKeyword()));
            specs.add(byLabel(criteria.getLabel()));
            specs.add(byCreatorId(createdId));
            specs.add(byFavorite(criteria.getFavorite()));
            specs.add(byArchived(criteria.getArchived()));
        }

        return specs.stream()
                .filter(Objects::nonNull)
                .reduce(Specification.where(null), Specification::and);
    }

    private static Specification<Note> byTargetId(Long targetId) {
        return (root, query, cb) -> {
            if (targetId == null) return null;
            return cb.equal(root.get("targetId"), targetId);
        };
    }

    private static Specification<Note> byNoteType(String noteType) {
        return (root, query, cb) -> {
            if (noteType == null || noteType.isEmpty()) return null;
            return cb.equal(root.get("type").get("name"), noteType);
        };
    }


    private static Specification<Note> byNoteTypeId(Long noteTypeId) {
        return (root, query, cb) -> {
            if (noteTypeId == null) return null;
            return cb.equal(root.get("type").get("id"), noteTypeId);
        };
    }

    private static Specification<Note> byCreatedAt(LocalDate createdAt) {
        return (root, query, cb) -> {
            if (createdAt == null) return null;
            return cb.equal(cb.function("date", LocalDate.class, root.get("createdAt")), createdAt);
        };
    }

    private static Specification<Note> byKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) return null;
            String kw = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), kw),
                    cb.like(cb.lower(root.get("description")), kw)
            );
        };
    }

    private static Specification<Note> byLabel(String label) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(label)) return null;
            return cb.equal(cb.lower(root.get("label")), label.toLowerCase());
        };
    }

    private static Specification<Note> byCreatorId(Long creatorId) {
        return (root, query, cb) -> {
            if (creatorId == null) return null;
            return cb.equal(root.get("creator").get("id"), creatorId);
        };
    }

    private static Specification<Note> byFavorite(Boolean favorite) {
        return (root, query, cb) -> {
            if (favorite == null) return null;
            return cb.equal(root.get("favorite"), favorite);
        };
    }

    private static Specification<Note> byArchived(Boolean archived) {
        return (root, query, cb) -> {
            if (archived == null) return null;
            return cb.equal(root.get("archived"), archived);
        };
    }
}
