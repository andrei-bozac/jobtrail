<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="esteEditare" value="${not empty companie.id}"/>
<c:set var="titluPagina" value="${esteEditare ? 'Editează compania' : 'Companie nouă'}" scope="request"/>
<c:set var="meniuActiv" value="companii" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-8">

        <h1 class="h3 mb-3">${titluPagina}</h1>

        <c:if test="${not empty eroare}">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle"></i> <c:out value="${eroare}"/>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/companii/salveaza"
              class="card shadow-sm">
            <div class="card-body">

                <input type="hidden" name="id" value="${companie.id}">

                <div class="mb-3">
                    <label for="nume" class="form-label">Nume <span class="text-danger">*</span></label>
                    <input type="text" id="nume" name="nume" class="form-control"
                           maxlength="100" required
                           value="${fn:escapeXml(companie.nume)}">
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="domeniu" class="form-label">Domeniu</label>
                        <input type="text" id="domeniu" name="domeniu" class="form-control"
                               maxlength="100" placeholder="ex: Fintech"
                               value="${fn:escapeXml(companie.domeniu)}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="oras" class="form-label">Oraș</label>
                        <input type="text" id="oras" name="oras" class="form-control"
                               maxlength="100" placeholder="ex: Cluj-Napoca"
                               value="${fn:escapeXml(companie.oras)}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-8 mb-3">
                        <label for="website" class="form-label">Website</label>
                        <input type="text" id="website" name="website" class="form-control"
                               maxlength="255" placeholder="https://..."
                               value="${fn:escapeXml(companie.website)}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="dimensiune" class="form-label">Dimensiune</label>
                        <select id="dimensiune" name="dimensiune" class="form-select">
                            <option value="">— nespecificată —</option>
                            <c:forEach var="d" items="${dimensiuni}">
                                <option value="${d}" ${d == companie.dimensiune ? 'selected' : ''}>${d}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="note" class="form-label">Note</label>
                    <textarea id="note" name="note" class="form-control" rows="4"><c:out value="${companie.note}"/></textarea>
                </div>

            </div>
            <div class="card-footer bg-white d-flex justify-content-end gap-2">
                <a href="${pageContext.request.contextPath}/companii" class="btn btn-outline-secondary">Anulează</a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> Salvează
                </button>
            </div>
        </form>

    </div>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>