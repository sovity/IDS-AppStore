package io.dataspaceconnector.controller.gui;

import de.fraunhofer.fit.appstore.model.search.SearchResult;
import de.fraunhofer.fit.appstore.services.search.SearchAppService;
import io.dataspaceconnector.common.util.Utils;
import io.dataspaceconnector.controller.resource.base.tag.ResourceDescription;
import io.dataspaceconnector.controller.util.ResponseCode;
import io.dataspaceconnector.controller.util.ResponseDescription;
import io.dataspaceconnector.service.resource.type.AppService;
import io.dataspaceconnector.service.resource.type.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@CrossOrigin(origins = {"https://drm-appstore.fit.fraunhofer.de", "http://drm-appstore.fit.fraunhofer.de"})
@RequiredArgsConstructor
@RequestMapping("/api/gui")
@Tag(name="AppStore GUI")
public class AppGuiController {

    private final @NonNull AppService appService;

    private final @NonNull ResourceService resourceService;

    /**
     * The service managing apps.
     */
    private final @NonNull SearchAppService appSearchService;

    /**
     * Search for apps.
     *
     * @param searchText The search text.
     * @param page       The number of pages.
     * @param size       The number of results per page.
     * @return Response with code 200 (Ok) and the search result.
     */
    @Tag(name = "UI", description = ResourceDescription.APPS)
    @GetMapping(value = "/search")
    @Operation(summary = "Search all Apps with value in description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ResponseCode.OK, description = ResponseDescription.OK)})
    public ResponseEntity<Object> searchApps(
            @RequestParam(value = "search") final String searchText,
            @RequestParam(value = "page", required = false,
                    defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false,
                    defaultValue = "30") final Integer size) {

        final var resultsCount = appSearchService.searchAppsResultCount(searchText);
        final var pageCount = appSearchService.searchAppsPagesCount(searchText, size);
        final var appList = appSearchService.searchAppsByDescription(searchText, page, size);

        final var result = new SearchResult();
        result.setPageNo(page);
        result.setResultsCount(resultsCount);
        result.setPageCount(pageCount);
        result.setAppList(appList);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "UI", description = ResourceDescription.APPS)
    @GetMapping(value = "/list")
    @Operation(summary = "Search all Apps with value in description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ResponseCode.OK, description = ResponseDescription.OK)})
    public ResponseEntity<Object> getApps(
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "30") final Integer size) {
        var pageable = Utils.toPageRequest(page, size);
        var apps = resourceService.getAll(Pageable.unpaged());

        //PagedModel<Resource> pgm = new PagedModel(apps.getContent(), new PagedModel.PageMetadata(apps.getSize(), apps.getNumber(), apps.getTotalElements(), apps.getTotalPages()));

        return new ResponseEntity<>(apps, HttpStatus.OK);
    }

}