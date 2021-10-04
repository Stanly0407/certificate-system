package com.epam.esm.controllers;

import com.epam.esm.entities.Entity;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class LinkBuilder {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PAGE_PARAM = "page";

    public Map<String, Object> getPaginationInfo(long pageQuantity, int pageNumber) {
        Map<String, Object> pages = new HashMap<>();
        Integer previousPage = null;
        Integer nextPage = null;
        if ((pageNumber - 1) > 0) {
            previousPage = pageNumber - 1;
        }
        if ((pageNumber + 1) <= pageQuantity) {
            nextPage = pageNumber + 1;
        }

        if (previousPage != null) {
            pages.put(PREVIOUS_PAGE, previousPage);
        }
        if (nextPage != null) {
            pages.put(NEXT_PAGE, nextPage);
        }
        return pages;
    }

    public List<Link> createPaginationLinks(Long pageQuantity, Integer pageNumber, String uriString) {
        List<Link> links = new ArrayList<>();
        Map<String, Object> params = getPaginationInfo(pageQuantity, pageNumber);
        Link currentPageLink = Link.of(uriString).withSelfRel();
        links.add(currentPageLink);

        if (params.containsKey(PREVIOUS_PAGE)) {
            int previousPage = (int) params.get(PREVIOUS_PAGE);
            params.put(PAGE_PARAM, previousPage);
            String uri = UriComponentsBuilder
                    .fromUriString(uriString)
                    .replaceQueryParam(PAGE_PARAM, previousPage)
                    .buildAndExpand().encode().toUriString();
            Link previousPageLink = Link.of(uri).withRel(PREVIOUS_PAGE);
            links.add(previousPageLink);
        }
        if (params.containsKey(NEXT_PAGE)) {
            int nextPage = (int) params.get(NEXT_PAGE);
            params.put(PAGE_PARAM, nextPage);
            String uri = UriComponentsBuilder
                    .fromUriString(uriString)
                    .replaceQueryParam(PAGE_PARAM, nextPage)
                    .buildAndExpand().toUriString();
            Link nextPageLink = Link.of(uri).withRel(NEXT_PAGE);
            links.add(nextPageLink);
        }
        return links;
    }

    public void addSelfLinks(List<? extends Entity> entities, Class<? extends BaseController> controller) {
        entities.forEach(e -> e.add(getSelfLink(e.getId(), controller)));
    }

    public void addSelfLink(RepresentationModel<?> entity, Long id, Class<? extends BaseController> controller) {
        entity.add(getSelfLink(id, controller));
    }

    public Link getSelfLink(Long id, Class<? extends BaseController> controller) {
        return linkTo(controller).slash(id).withSelfRel().expand();
    }

}
