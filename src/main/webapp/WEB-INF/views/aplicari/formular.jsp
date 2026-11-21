<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="esteEditare" value="${not empty aplicare.id}"/>
<c:set var="titluPagina" value="${esteEditare ? 'Editează aplicarea' : 'Aplicare nouă'}" scope="request"/>
<c:set var="meniuActiv" value="aplicari" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-9">

        <h1 class="h3 mb-3">${titluPagina}</h1>

        <c:if test="${not empty eroare}">
            <div class="alert alert-danger">
                <i class="bi bi-exclamation-triangle"></i> <c:out value="${eroare}"/>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/aplicari/salveaza" class="card shadow-sm">
            <div class="card-body">
                <input type="hidden" name="id" value="${aplicare.id}">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="companieId" class="form-label">Companie <span class="text-danger">*</span></label>
                        <select id="companieId" name="companieId" class="form-select" required>
                            <option value="">— alege compania —</option>
                            <c:forEach var="c" items="${companii}">
                                <option value="${c.id}" ${c.id == aplicare.companieId ? 'selected' : ''}>
                                    <c:out value="${c.nume}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="pozitie" class="form-label">Poziție <span class="text-danger">*</span></label>
                        <input type="text" id="pozitie" name="pozitie" class="form-control" maxlength="150" required
                               placeholder="ex: Java Intern" value="${fn:escapeXml(aplicare.pozitie)}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="status" class="form-label">Status <span class="text-danger">*</span></label>
                        <select id="status" name="status" class="form-select" required>
                            <c:forEach var="s" items="${statusuri}">
                                <option value="${s}" ${s == aplicare.status ? 'selected' : ''}>${s.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="sursa" class="form-label">Sursă <span class="text-danger">*</span></label>
                        <select id="sursa" name="sursa" class="form-select" required>
                            <option value="">— alege —</option>
                            <c:forEach var="s" items="${surse}">
                                <option value="${s}" ${s == aplicare.sursa ? 'selected' : ''}>${s.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="tip" class="form-label">Tip <span class="text-danger">*</span></label>
                        <select id="tip" name="tip" class="form-select" required>
                            <option value="">— alege —</option>
                            <c:forEach var="t" items="${tipuri}">
                                <option value="${t}" ${t == aplicare.tip ? 'selected' : ''}>${t.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="modLucru" class="form-label">Mod de lucru</label>
                        <select id="modLucru" name="modLucru" class="form-select">
                            <option value="">— nespecificat —</option>
                            <c:forEach var="m" items="${moduriLucru}">
                                <option value="${m}" ${m == aplicare.modLucru ? 'selected' : ''}>${m.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="dataAplicarii" class="form-label">Data aplicării <span class="text-danger">*</span></label>
                        <input type="date" id="dataAplicarii" name="dataAplicarii" class="form-control" required
                               max="${azi}" value="${aplicare.dataAplicarii}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="salariu" class="form-label">Salariu (lei)</label>
                        <input type="number" id="salariu" name="salariu" class="form-control"
                               min="0" step="0.01" value="${aplicare.salariu}">
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="linkAnunt" class="form-label">Link anunț</label>
                        <input type="text" id="linkAnunt" name="linkAnunt" class="form-control" maxlength="500"
                               placeholder="https://..." value="${fn:escapeXml(aplicare.linkAnunt)}">
                    </div>
                </div>

                <div class="mb-3">
                    <label for="note" class="form-label">Note</label>
                    <textarea id="note" name="note" class="form-control" rows="3"><c:out value="${aplicare.note}"/></textarea>
                </div>
            </div>

            <div class="card-footer bg-white d-flex justify-content-end gap-2">
                <c:choose>
                    <c:when test="${esteEditare}">
                        <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${aplicare.id}" class="btn btn-outline-secondary">Anulează</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/aplicari" class="btn btn-outline-secondary">Anulează</a>
                    </c:otherwise>
                </c:choose>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> Salvează</button>
            </div>
        </form>

    </div>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>