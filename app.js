// Lumina Global Application Scripts
document.addEventListener('DOMContentLoaded', () => {
    // Back to top smooth scroll
    const backToTopBtn = document.getElementById('back-to-top');
    if (backToTopBtn) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 300) {
                backToTopBtn.style.opacity = '1';
                backToTopBtn.style.pointerEvents = 'auto';
            } else {
                backToTopBtn.style.opacity = '0';
                backToTopBtn.style.pointerEvents = 'none';
            }
        });

        backToTopBtn.addEventListener('click', (e) => {
            e.preventDefault();
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    // Prevent any empty links or unhandled form submissions from triggering reloads
    document.querySelectorAll('a[href="#"]').forEach(a => {
        a.addEventListener('click', (e) => {
            e.preventDefault();
        });
    });
});

