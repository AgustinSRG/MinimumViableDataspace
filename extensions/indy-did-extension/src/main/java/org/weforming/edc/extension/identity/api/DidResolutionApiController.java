/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - Initial implementation
 *
 */

package org.weforming.edc.extension.identity.api;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.edc.iam.did.spi.document.DidDocument;
import org.eclipse.edc.spi.monitor.Monitor;
import org.eclipse.edc.spi.result.Result;
import org.weforming.edc.extension.identity.resolution.IndyBesuDidResolver;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class DidResolutionApiController {

    private final Monitor monitor;

    private final IndyBesuDidResolver resolver;

    public DidResolutionApiController(IndyBesuDidResolver resolver, Monitor monitor) {
        this.resolver = resolver;
        this.monitor = monitor;
    }

    @GET
    @Path("did/resolve/{did}")
    public Response resolveDid(@PathParam("did") String did) {
        Result<DidDocument> result = resolver.resolve(did);

        if (result.failed()) {
            return Response.status(404).type(MediaType.APPLICATION_JSON).entity("{\"message\":\"The provided DID cannot be resolved\"}").build();
        }

        return Response.ok().type(MediaType.APPLICATION_JSON).entity(result.getContent()).build();
    }
}
